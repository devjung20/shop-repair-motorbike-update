import { generateApi } from "../../utils/api";

const orderAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/orders", params, data);
  },

  create: (data, params) => {
    return generateApi("post", `api/orders`, params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/orders/${id}`, params, data);
  },

  updateById: (id, data, params) => {
    return generateApi("put", `api/orders/${id}`, params, data);
  },

  deleteById: (id, params, data) => {
    return generateApi("delete", `api/orders/${id}`, params, data);
  },

  toInvoice: (id, params, data) => {
    return generateApi("put", `api/orders/toInvoice/${id}`, params, data);
  },
};

export default orderAPI;
