import { generateApi } from "../../utils/api";

const productAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/products", params, data);
  },

  create: (data, params) => {
    return generateApi("post", `api/products`, params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/products/${id}`, params, data);
  },

  updateById: (id, data, params) => {
    return generateApi("put", `api/products/${id}`, params, data);
  },

  deleteById: (id, params, data) => {
    return generateApi("delete", `api/products/${id}`, params, data);
  },

  getHistory: (params, data) => {
    return generateApi("get", "api/history", params, data);
  },

  createHistory: (data, params) => {
    return generateApi("post", "api/history", params, data);
  },
};

export default productAPI;
