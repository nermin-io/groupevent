import axios from "axios";

const Proxy = axios.create({
  baseURL: '/api',
  timeout: 15000
});

export default Proxy;
