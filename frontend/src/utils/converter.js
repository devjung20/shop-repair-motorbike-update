export const toYYYY_MM_DD = (date = new Date()) => {
  date = new Date(date);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return [year, month, day].join("-");
};

export const toDDMMYYYY = (date = new Date()) => {
  date = new Date(date);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return [day, month, year].join("/");
};

export const toNumber = (inputString) => {
  if (!inputString) return 0;
  const numericString = inputString.replace(/[^0-9.]/g, "");
  return parseFloat(numericString);
};

export const intToString = (value) => {
  if (value === null) return "";
  return `${parseInt(value, 10)}`;
};

export const stringToInt = (value) => {
  const parsedValue = parseInt(value, 10);
  if (isNaN(parsedValue)) return 1;
  return parsedValue;
};
