import React, { useState } from 'react';
import { createSubscription } from '../services/api';

const CreateSubscription = () => {
    const [hotelID, setHotelID] = useState('');
    const [startDate, setStartDate] = useState('');
    const [term, setTerm] = useState('MONTHLY');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const subscription = {
            hotelID,
            startDate,
            term,
            status: 'ACTIVE',
        };
        await createSubscription(subscription);
        setHotelID('');
        setStartDate('');
        setTerm('MONTHLY');
    };

    return (
        <div className="container mt-4">
            <h1>Create Subscription</h1>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Hotel ID</label>
                    <input type="number" className="form-control" value={hotelID} onChange={(e) => setHotelID(e.target.value)} required />
                </div>
                <div className="mb-3">
                    <label className="form-label">Start Date</label>
                    <input type="date" className="form-control" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
                </div>
                <div className="mb-3">
                    <label className="form-label">Term</label>
                    <select className="form-select" value={term} onChange={(e) => setTerm(e.target.value)}>
                        <option value="MONTHLY">MONTHLY</option>
                        <option value="YEARLY">YEARLY</option>
                    </select>
                </div>
                <button type="submit" className="btn btn-primary">Create</button>
            </form>
        </div>
    );
};

export default CreateSubscription;