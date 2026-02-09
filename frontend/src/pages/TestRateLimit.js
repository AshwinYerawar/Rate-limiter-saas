import React, { useState } from 'react';

const TestRateLimit = () => {
  const [formData, setFormData] = useState({
    endpoint: '/api/users',
    httpMethod: 'GET',
    apiKey: 'test-api-key-123'
  });
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await fetch('/api/v1/rate-limit/check', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-API-Key': formData.apiKey
        },
        body: JSON.stringify({
          endpoint: formData.endpoint,
          httpMethod: formData.httpMethod
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const makeMultipleRequests = async (count) => {
    setLoading(true);
    setError(null);
    setResult(null);

    const results = [];
    for (let i = 0; i < count; i++) {
      try {
        const response = await fetch('/api/v1/rate-limit/check', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-API-Key': formData.apiKey
          },
          body: JSON.stringify({
            endpoint: formData.endpoint,
            httpMethod: formData.httpMethod
          })
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        results.push(data);

        // Small delay between requests
        await new Promise(resolve => setTimeout(resolve, 100));
      } catch (err) {
        results.push({ error: err.message });
      }
    }

    setResult(results);
    setLoading(false);
  };

  return (
    <div>
      <h1>Test Rate Limiting</h1>
      <p>Test and demonstrate rate limiting algorithms in real-time</p>

      <div className="rate-limiter-container">
        <div className="form-section">
          <h2>Test Rate Limiting</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="endpoint">Endpoint:</label>
              <input
                type="text"
                id="endpoint"
                name="endpoint"
                value={formData.endpoint}
                onChange={handleInputChange}
                placeholder="/api/users"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="httpMethod">HTTP Method:</label>
              <select
                id="httpMethod"
                name="httpMethod"
                value={formData.httpMethod}
                onChange={handleInputChange}
              >
                <option value="GET">GET</option>
                <option value="POST">POST</option>
                <option value="PUT">PUT</option>
                <option value="DELETE">DELETE</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="apiKey">API Key:</label>
              <input
                type="text"
                id="apiKey"
                name="apiKey"
                value={formData.apiKey}
                onChange={handleInputChange}
                placeholder="test-api-key-123"
                required
              />
            </div>

            <button type="submit" className="btn" disabled={loading}>
              {loading ? 'Testing...' : 'Test Single Request'}
            </button>
          </form>

          <div style={{ marginTop: '20px' }}>
            <h3>Bulk Test</h3>
            <p>Make multiple requests to test rate limiting:</p>
            <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
              <button
                className="btn"
                onClick={() => makeMultipleRequests(5)}
                disabled={loading}
                style={{ flex: '1', minWidth: '120px' }}
              >
                Test 5 Requests
              </button>
              <button
                className="btn"
                onClick={() => makeMultipleRequests(10)}
                disabled={loading}
                style={{ flex: '1', minWidth: '120px' }}
              >
                Test 10 Requests
              </button>
              <button
                className="btn"
                onClick={() => makeMultipleRequests(20)}
                disabled={loading}
                style={{ flex: '1', minWidth: '120px' }}
              >
                Test 20 Requests
              </button>
            </div>
          </div>
        </div>

        <div className="results-section">
          <h2>Results</h2>

          {error && (
            <div className="error">
              <strong>Error:</strong> {error}
            </div>
          )}

          {loading && (
            <div className="loading">
              <p>Testing rate limits...</p>
            </div>
          )}

          {result && (
            <div className="results">
              {Array.isArray(result) ? (
                <div>
                  <h3>Bulk Test Results ({result.length} requests)</h3>
                  {result.map((item, index) => (
                    <div key={index} className={`result-item ${item.allowed ? 'allowed' : 'blocked'}`}>
                      <div><strong>Request {index + 1}:</strong></div>
                      {item.error ? (
                        <div>Error: {item.error}</div>
                      ) : (
                        <div>
                          <div>Allowed: {item.allowed ? '✅ Yes' : '❌ No'}</div>
                          <div>Limit: {item.limit}</div>
                          <div>Remaining: {item.remaining}</div>
                          <div>Algorithm: {item.algorithm}</div>
                          <div>Reset At: {new Date(item.resetAt).toLocaleString()}</div>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <div className={`result-item ${result.allowed ? 'allowed' : 'blocked'}`}>
                  <div><strong>Single Request Result:</strong></div>
                  <div>Allowed: {result.allowed ? '✅ Yes' : '❌ No'}</div>
                  <div>Limit: {result.limit}</div>
                  <div>Remaining: {result.remaining}</div>
                  <div>Algorithm: {result.algorithm}</div>
                  <div>Reset At: {new Date(result.resetAt).toLocaleString()}</div>
                </div>
              )}
            </div>
          )}

          {!result && !loading && !error && (
            <div className="loading">
              <p>Configure your request parameters and click "Test" to see rate limiting in action!</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TestRateLimit;