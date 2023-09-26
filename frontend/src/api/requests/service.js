import { generateApi } from "../../utils/api";

const serviceAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/services", params, data);
  },

  create: (data, params) => {
    return generateApi("post", "api/services", params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/services/${id}`, params, data);
  },
  updateById: (id, data, params) => {
    return generateApi("put", `api/services/${id}`, params, data);
  },
  deleteById: (id, params, data) => {
    return generateApi("delete", `api/services/${id}`, params, data);
  },
};

export default serviceAPI;
