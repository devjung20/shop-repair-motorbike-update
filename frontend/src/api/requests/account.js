import { generateApi } from "../../utils/api";

const accountAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/accounts", params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/accounts/${id}`, params, data);
  },

  updateById: (id, data, params) => {
    return generateApi("put", `api/accounts/${id}`, params, data);
  },

  deleteById: (id, params, data) => {
    return generateApi("delete", `api/accounts/${id}`, params, data);
  },
};

export default accountAPI;
