import { generateApi } from "../../utils/api";

const storeAPI = {
  getStore: (params, data) => {
    return generateApi("get", "api/stores", params, data);
  },

  updateStore: (data, params) => {
    return generateApi("post", `api/stores`, params, data);
  },
};

export default storeAPI;
