import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Tenants = () => {
  const [tenants, setTenants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingTenant, setEditingTenant] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    fetchTenants();
  }, []);

  const fetchTenants = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/v1/tenants');
      setTenants(response.data);
    } catch (err) {
      setError('Failed to load tenants');
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
      if (editingTenant) {
        await axios.put(`/api/v1/tenants/${editingTenant.id}`, formData);
      } else {
        await axios.post('/api/v1/tenants', formData);
      }

      fetchTenants();
      resetForm();
    } catch (err) {
      setError('Failed to save tenant');
      console.error(err);
    }
  };

  const handleEdit = (tenant) => {
    setEditingTenant(tenant);
    setFormData({
      name: tenant.name,
      status: tenant.status
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this tenant?')) {
      try {
        await axios.delete(`/api/v1/tenants/${id}`);
        fetchTenants();
      } catch (err) {
        setError('Failed to delete tenant');
        console.error(err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      status: 'ACTIVE'
    });
    setEditingTenant(null);
    setShowForm(false);
  };

  if (loading) {
    return <div className="loading">Loading tenants...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Tenants</h1>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add Tenant'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <div className="form-container">
          <h2>{editingTenant ? 'Edit Tenant' : 'Create New Tenant'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Tenant Name:</label>
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
              <label htmlFor="status">Status:</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleInputChange}
              >
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
                <option value="SUSPENDED">Suspended</option>
              </select>
            </div>

            <div className="form-actions">
              <button type="submit" className="btn btn-primary">
                {editingTenant ? 'Update Tenant' : 'Create Tenant'}
              </button>
              <button type="button" className="btn" onClick={resetForm}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="tenants-list">
        <h2>All Tenants</h2>
        {tenants.length === 0 ? (
          <p>No tenants found. Create your first tenant!</p>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Status</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tenants.map(tenant => (
                  <tr key={tenant.id}>
                    <td>{tenant.name}</td>
                    <td>
                      <span className={`status status-${tenant.status.toLowerCase()}`}>
                        {tenant.status}
                      </span>
                    </td>
                    <td>{new Date(tenant.createdAt).toLocaleDateString()}</td>
                    <td>
                      <button
                        className="btn btn-small"
                        onClick={() => handleEdit(tenant)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-small btn-danger"
                        onClick={() => handleDelete(tenant.id)}
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

export default Tenants;