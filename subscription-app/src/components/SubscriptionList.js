import React, { useEffect, useState } from 'react';
import { filterSubscriptions } from '../services/api';
import { Link } from 'react-router-dom';

const SubscriptionList = () => {
    const [subscriptions, setSubscriptions] = useState([]);
    const [status, setStatus] = useState('');
    const [startDateMonth, setStartDateMonth] = useState('');

    useEffect(() => {
        fetchSubscriptions();
    }, []);

    const fetchSubscriptions = async () => {
    //   await getAllSubscriptions();
       // setSubscriptions(response.data);
        setSubscriptions([{
            subscriptionID: "1",
            hotelID: "1",
            subscription: "Active",
            nextPayment: "Tomo"

        }])
    };

    const cancelSubscription = async () => {
        console.log("Eror");
      };

    const handleFilter = async () => {
        const response = await filterSubscriptions(status, startDateMonth);
        setSubscriptions(response.data);
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Subscriptions</h1>
            <div className="mb-4">
                <label className="form-label me-2">
                    Status:
                    <select className="form-select" value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="EXPIRED">EXPIRED</option>
                        <option value="CANCELED">CANCELED</option>
                    </select>
                </label>
                <label className="form-label me-2">
                    Start Date Month:
                    <input className="form-control" type="month" value={startDateMonth} onChange={(e) => setStartDateMonth(e.target.value)} />
                </label>
                <button className="btn btn-primary" onClick={handleFilter}>Filter</button>
            </div>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>Hotel ID</th>
                        <th>Status</th>
                        <th>Next Payment</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {subscriptions.map((subscription) => (
                        <tr key={subscription.subscriptionID}>
                            <td>{subscription.hotelID}</td>
                            <td>{subscription.status}</td>
                            <td>{subscription.nextPayment}</td>
                            <td>
                                <Link to={`/subscription/${subscription.subscriptionID}`} className="btn btn-info me-2">View</Link>
                                <button className="btn btn-danger" onClick={() => cancelSubscription(subscription.subscriptionID)}>Cancel</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default SubscriptionList;