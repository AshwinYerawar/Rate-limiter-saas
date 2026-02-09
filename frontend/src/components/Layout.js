import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Layout.css';

const Layout = ({ children }) => {
  const location = useLocation();

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="nav-brand">
          <Link to="/">🚦 Rate Limiter SaaS</Link>
        </div>
        <ul className="nav-links">
          <li>
            <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
              Dashboard
            </Link>
          </li>
          <li>
            <Link to="/tenants" className={location.pathname === '/tenants' ? 'active' : ''}>
              Tenants
            </Link>
          </li>
          <li>
            <Link to="/policies" className={location.pathname === '/policies' ? 'active' : ''}>
              Policies
            </Link>
          </li>
          <li>
            <Link to="/api-keys" className={location.pathname === '/api-keys' ? 'active' : ''}>
              API Keys
            </Link>
          </li>
          <li>
            <Link to="/test" className={location.pathname === '/test' ? 'active' : ''}>
              Test Rate Limiting
            </Link>
          </li>
        </ul>
      </nav>
      <main className="main-content">
        {children}
      </main>
    </div>
  );
};

export default Layout;