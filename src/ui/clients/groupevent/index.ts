import axios from "axios";

export const DEVELOPMENT_URL = 'http://localhost:8080/api/v1';
export const PRODUCTION_URL = 'https://api.groupevent.co/api/v1';

const Groupevent = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? DEVELOPMENT_URL : PRODUCTION_URL,
  timeout: 15000,
  headers: { "X-API-KEY": `${process.env.GROUPEVENT_API_KEY}` },
});

export default Groupevent;
