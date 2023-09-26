import { generateApi } from "../../utils/api";

const statisticAPI = {
  getServiceStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/services", params, data);
  },
  getProductInStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-in", params, data);
  },
  getProductOutStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-out", params, data);
  },
  getProductOutInStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-out-in", params, data);
  },
  getProductInYearStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-in-year", params, data);
  },
  getProductOutYearStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-out-year", params, data);
  },
  getProductOutInYearStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/products-out-in-year", params, data);
  },
  getServiceYearStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/services-year", params, data);
  },
  getSalaryStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/employees-salary", params, data);
  },
  getTopProductStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/top-product", params, data);
  },
  getTopServiceStatistic: (params, data) => {
    return generateApi("get", "/api/statistics/top-service", params, data);
  },
};

export default statisticAPI;
