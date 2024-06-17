import React, { useEffect, useState } from 'react';
import { createSubscription, getAllHotels } from '../services/api';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { toast } from 'react-toastify';

const CreateSubscription = () => {
  const [hotels, setHotels] = useState([]);
  const [selectedHotel, setSelectedHotel] = useState('');
  const [startDate, setStartDate] = useState('');
  const [term, setTerm] = useState('MONTHLY');

  useEffect(() => {
    getAllHotels()
      .then(response => {
        setHotels(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the hotels!', error);
      });
  }, []);

  const getCurrentDate = () => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  };

  const handleHotelChange = (event) => {
    setSelectedHotel(event.target.value);
  };

  const handleSubmit = (event) => {
    console.log("Hello")
    event.preventDefault();

    const subscriptionData = {
      hotelid: selectedHotel,
      startDate: startDate,
      term: term
    };

    // Send the subscription data to the backend to create a subscription
    createSubscription(subscriptionData)
      .then(response => {
        toast.success('Subscription created successfully');
      })
      .catch(error => {
        toast.error(error?.response?.data?.message);
        console.error('There was an error creating the subscription!', error);
      });
  };

  return (
    <div>
         <ToastContainer />
      <h2>Create Subscription</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Choose a hotel</label>
          <select
            className="form-select"
            value={selectedHotel}
            onChange={handleHotelChange}
            required
          >
            <option value="">Select a hotel</option>
            {hotels.map(hotel => (
              <option key={hotel.hotelID} value={hotel.hotelID}>
                {hotel.hotelName}
              </option>
            ))}
          </select>
        </div>
        <div className="mb-3">
          <label className="form-label">Start Date</label>
          <input
            type="date"
            className="form-control"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            min={getCurrentDate()}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Term</label>
          <select
            className="form-select"
            value={term}
            onChange={(e) => setTerm(e.target.value)}
          >
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