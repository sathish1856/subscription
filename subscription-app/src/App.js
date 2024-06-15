import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import SubscriptionList from './components/SubscriptionList';
import CreateSubscription from './components/createSubscription';
import SubscriptionDetail from './components/SubscriptionDetail';

function App() {
    return (
        <Router>
            <div>
                <nav className="navbar navbar-expand-lg navbar-light bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" href="/">Hotel Subscriptions</a>
                        <div className="collapse navbar-collapse" id="navbarNav">
                            <ul className="navbar-nav">
                                <li className="nav-item">
                                    <Link className="nav-link" to="/">Home</Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/create">Create Subscription</Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div className="container mt-4">
                    <Routes>
                        <Route path="/" element={<SubscriptionList />} />
                        <Route path="/create" element={<CreateSubscription />} />
                        <Route path="/subscription/:id" element={<SubscriptionDetail />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
