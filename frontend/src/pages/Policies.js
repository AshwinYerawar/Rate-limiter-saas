import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Policies = () => {
  const [policies, setPolicies] = useState([]);
  const [tenants, setTenants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingPolicy, setEditingPolicy] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    algorithm: 'TOKEN_BUCKET',
    limit: 100,
    windowSeconds: 60,
    tenantId: ''
  });

  const algorithms = [
    { value: 'TOKEN_BUCKET', label: 'Token Bucket' },
    { value: 'LEAKY_BUCKET', label: 'Leaky Bucket' },
    { value: 'FIXED_WINDOW', label: 'Fixed Window' },
    { value: 'SLIDING_WINDOW', label: 'Sliding Window' }
  ];

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [policiesRes, tenantsRes] = await Promise.all([
        axios.get('/api/v1/policies'),
        axios.get('/api/v1/tenants')
      ]);

      setPolicies(policiesRes.data);
      setTenants(tenantsRes.data);
    } catch (err) {
      setError('Failed to load data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const tenant = tenants.find(t => t.id === formData.tenantId);
      const policyData = {
        ...formData,
        tenant: tenant
      };

      if (editingPolicy) {
        await axios.put(`/api/v1/policies/${editingPolicy.id}`, policyData);
      } else {
        await axios.post('/api/v1/policies', policyData);
      }

      fetchData();
      resetForm();
    } catch (err) {
      setError('Failed to save policy');
      console.error(err);
    }
  };

  const handleEdit = (policy) => {
    setEditingPolicy(policy);
    setFormData({
      name: policy.name,
      algorithm: policy.algorithm,
      limit: policy.limit,
      windowSeconds: policy.windowSeconds,
      tenantId: policy.tenant.id
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this policy?')) {
      try {
        await axios.delete(`/api/v1/policies/${id}`);
        fetchData();
      } catch (err) {
        setError('Failed to delete policy');
        console.error(err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      algorithm: 'TOKEN_BUCKET',
      limit: 100,
      windowSeconds: 60,
      tenantId: ''
    });
    setEditingPolicy(null);
    setShowForm(false);
  };

  if (loading) {
    return <div className="loading">Loading policies...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Rate Limit Policies</h1>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add Policy'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <div className="form-container">
          <h2>{editingPolicy ? 'Edit Policy' : 'Create New Policy'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Policy Name:</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="algorithm">Algorithm:</label>
              <select
                id="algorithm"
                name="algorithm"
                value={formData.algorithm}
                onChange={handleInputChange}
                required
              >
                {algorithms.map(algo => (
                  <option key={algo.value} value={algo.value}>
                    {algo.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="limit">Limit:</label>
              <input
                type="number"
                id="limit"
                name="limit"
                value={formData.limit}
                onChange={handleInputChange}
                min="1"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="windowSeconds">Window (seconds):</label>
              <input
                type="number"
                id="windowSeconds"
                name="windowSeconds"
                value={formData.windowSeconds}
                onChange={handleInputChange}
                min="1"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="tenantId">Tenant:</label>
              <select
                id="tenantId"
                name="tenantId"
                value={formData.tenantId}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Tenant</option>
                {tenants.map(tenant => (
                  <option key={tenant.id} value={tenant.id}>
                    {tenant.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-actions">
              <button type="submit" className="btn btn-primary">
                {editingPolicy ? 'Update Policy' : 'Create Policy'}
              </button>
              <button type="button" className="btn" onClick={resetForm}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="policies-list">
        <h2>All Policies</h2>
        {policies.length === 0 ? (
          <p>No policies found. Create your first policy!</p>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Algorithm</th>
                  <th>Limit</th>
                  <th>Window</th>
                  <th>Tenant</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {policies.map(policy => (
                  <tr key={policy.id}>
                    <td>{policy.name}</td>
                    <td>{algorithms.find(a => a.value === policy.algorithm)?.label}</td>
                    <td>{policy.limit}</td>
                    <td>{policy.windowSeconds}s</td>
                    <td>{policy.tenant.name}</td>
                    <td>
                      <button
                        className="btn btn-small"
                        onClick={() => handleEdit(policy)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-small btn-danger"
                        onClick={() => handleDelete(policy.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default Policies;