import axios from 'axios';

const API_URL = 'http://localhost:8080/v1';

const getAllSubscriptions = () => axios.get(`${API_URL}/subscriptions`);
const getSubscriptionById = (id) => axios.get(`${API_URL}/subscriptions/${id}`);
const createSubscription = (data) => axios.post(`${API_URL}/subscriptions`, data);
const cancelSubscription = (id) => axios.post(`${API_URL}/subscriptions/cancel/${id}`);
const restartSubscription = (id) => axios.post(`${API_URL}/subscriptions/restart/${id}`);
const filterSubscriptions = (status) => axios.get(`${API_URL}/subscriptions/filter/${status}`);
const getAuditLogs = (id) => axios.get(`${API_URL}/subscriptions/${id}/audits`);
const getAllHotels = () => axios.get(`${API_URL}/hotels`);

export {
    getAllSubscriptions,
    getSubscriptionById,
    createSubscription,
    cancelSubscription,
    restartSubscription,
    filterSubscriptions,
    getAuditLogs,
    getAllHotels
};
