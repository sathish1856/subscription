import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getSubscriptionById, cancelSubscription, restartSubscription } from '../services/api';

const SubscriptionDetail = () => {
    const { id } = useParams();
    const [subscription, setSubscription] = useState(null);

    useEffect(() => {
        fetchSubscription();
    }, [id]);

    const fetchSubscription = async () => {
        const response = await getSubscriptionById(id);
        setSubscription(response.data);
    };

    const handleCancel = async () => {
        await cancelSubscription(id);
        fetchSubscription();
    };

    const handleRestart = async () => {
        await restartSubscription(id);
        fetchSubscription();
    };

    if (!subscription) {
        return <div>Loading...</div>;
    }

    return (
        <div className="container mt-4">
            <h1>Subscription Detail</h1>
            <p><strong>Hotel ID:</strong> {subscription.hotelID}</p>
            <p><strong>Status:</strong> {subscription.status}</p>
            <p><strong>Next Payment:</strong> {subscription.nextPayment}</p>
            <button className="btn btn-danger me-2" onClick={handleCancel}>Cancel</button>
            <button className="btn btn-primary" onClick={handleRestart}>Restart</button>
        </div>
    );
};

export default SubscriptionDetail;
