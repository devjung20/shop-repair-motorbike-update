import { generateApi } from "../../utils/api";

const customerAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/customers", params, data);
  },

  create: (data, params) => {
    return generateApi("post", `api/customers`, params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/customers/${id}`, params, data);
  },

  updateById: (id, data, params) => {
    return generateApi("put", `api/customers/${id}`, params, data);
  },

  deleteById: (id, params, data) => {
    return generateApi("delete", `api/customers/${id}`, params, data);
  },
};

export default customerAPI;
