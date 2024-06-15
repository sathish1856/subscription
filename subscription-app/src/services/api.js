import axios from 'axios';

const API_URL = 'http://localhost:8080'; // Replace with your actual API URL

const getAllSubscriptions = () => axios.get(`${API_URL}/subscriptions`);
const getSubscriptionById = (id) => axios.get(`${API_URL}/subscriptions/${id}`);
const createSubscription = (data) => axios.post(`${API_URL}/subscriptions`, data);
const cancelSubscription = (id) => axios.delete(`${API_URL}/subscriptions/${id}`);
const restartSubscription = (id) => axios.put(`${API_URL}/subscriptions/${id}/restart`);
const filterSubscriptions = (status, startDateMonth) => axios.get(`${API_URL}/subscriptions/filter`, {
    params: { status, startDateMonth }
});
const getAuditLogs = (id) => axios.get(`${API_URL}/subscriptions/${id}/audits`);

export {
    getAllSubscriptions,
    getSubscriptionById,
    createSubscription,
    cancelSubscription,
    restartSubscription,
    filterSubscriptions,
    getAuditLogs
};
