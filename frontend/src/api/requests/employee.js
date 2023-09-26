import { generateApi } from "../../utils/api";

const employeeAPI = {
  getAll: (params, data) => {
    return generateApi("get", "api/employees", params, data);
  },

  create: (data, params) => {
    return generateApi("post", `api/employees`, params, data);
  },

  getById: (id, params, data) => {
    return generateApi("get", `api/employees/${id}`, params, data);
  },

  updateById: (id, data, params) => {
    return generateApi("put", `api/employees/${id}`, params, data);
  },

  deleteById: (id, params, data) => {
    return generateApi("delete", `api/employees/${id}`, params, data);
  },

  getAvailableUser: (params, data) => {
    return generateApi("get", "api/employees/getEmployeeNoAccount", params, data);
  },
};

export default employeeAPI;
