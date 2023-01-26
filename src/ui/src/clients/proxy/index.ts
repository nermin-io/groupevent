import axios from "axios";

const Proxy = axios.create({
  baseURL: "/api",
  timeout: 15000,
  validateStatus: function (status) {
    return status < 500;
  },
});

export default Proxy;
