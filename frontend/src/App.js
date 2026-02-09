import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Tenants from './pages/Tenants';
import Policies from './pages/Policies';
import ApiKeys from './pages/ApiKeys';
import TestRateLimit from './pages/TestRateLimit';
import './App.css';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/tenants" element={<Tenants />} />
          <Route path="/policies" element={<Policies />} />
          <Route path="/api-keys" element={<ApiKeys />} />
          <Route path="/test" element={<TestRateLimit />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;