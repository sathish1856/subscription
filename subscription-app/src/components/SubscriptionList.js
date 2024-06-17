import React, { useEffect, useState } from 'react';
import { getAllSubscriptions, filterSubscriptions } from '../services/api';
import { Link } from 'react-router-dom';

const SubscriptionList = () => {
    const [subscriptions, setSubscriptions] = useState([]);
    const [status, setStatus] = useState('ALL');
    const [startDateMonth, setStartDateMonth] = useState('');

    useEffect(() => {
        fetchSubscriptions();
    }, []);

    const fetchSubscriptions = async () => {
        const response = await getAllSubscriptions();
        setSubscriptions(response.data);
    };

    const cancelSubscription = async () => {
        console.log("Eror");
      };

    const handleFilter = async () => {
        const response = await filterSubscriptions(status);
        setSubscriptions(response.data);
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Subscriptions</h1>
            <div className="mb-4">
                <label className="form-label me-2">
                    Status:
                    <select className="form-select" value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="ALL">All</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="EXPIRED">EXPIRED</option>
                        <option value="CANCELED">CANCELED</option>
                    </select>
                </label>
                <button className="btn btn-primary" onClick={handleFilter}>Filter</button>
            </div>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>Hotel Name</th>
                        <th>Status</th>
                        <th>Next Payment</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {subscriptions.map((subscription) => (
                        <tr key={subscription.subscriptionID}>
                            <td>{subscription.hotel.hotelName}</td>
                            <td>{subscription.status}</td>
                            <td>{subscription.nextPayment}</td>
                            <td>
                                <Link to={`/subscription/${subscription.subscriptionID}`} className="btn btn-info me-2">View</Link>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default SubscriptionList;