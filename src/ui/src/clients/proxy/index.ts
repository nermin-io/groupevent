import axios from "axios";

const Proxy = axios.create({
  baseURL: "/api",
  timeout: 20000,
  validateStatus: function (status) {
    return status < 500;
  },
});

export default Proxy;
