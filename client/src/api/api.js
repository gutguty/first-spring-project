import axios from "axios";

//server port
const BASE_URL = "http://localhost:8080"

const api = axios.create({
    baseURL: BASE_URL,
    withCredentials: true
})

api.interceptors.request.use(
    (req) => {
        console.log(`Request ${req.method} ${req.url}`, req)
        return req
    },
    (error) => {
        console.error(error)
        return Promise.reject(error)
    }
)

api.interceptors.response.use(
    (res) => {
        console.log(`Response ${res.status} ${res.config.url}`, res)
        return res
    },
    (error) => {
        console.error(error)
        return Promise.reject(error)
    }
)

export const getAll = async () => {
    const response = await api.get('/api/cards');
    return response.data;
}

export const getCard = async (id) => {
    const response = await api.get(`/api/cards/${id}`);
    return response.data;
}

export const createCard = async (card) => {
    const response = await api.post('/api/cards', card);
    return response.data;
}

export const deleteCard = async (id) => {
    const response = await api.delete(`/api/cards/${id}`);
    return response.data;
}

export const updateCard = async (id, card) => {
    const response = await api.put(`/api/cards/${id}`,card);
    return response.data;
}