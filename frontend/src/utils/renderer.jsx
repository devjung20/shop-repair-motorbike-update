export const dateRenderer = (date = new Date()) => {
  date = new Date(date);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return `${day}/${month}/${year}`;
};

export const dateTimeRenderer = (date = new Date()) => {
  date = new Date(date);
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return `${hours}:${minutes}:${seconds} ${day}/${month}/${year}`;
};

export const moneyRenderer = (money = 0) => {
  return money.toLocaleString() + " ₫";
};

export const incomeRenderer = (income = 0) => {
  let sign = "";
  let color = "#808080";

  if (income > 0) {
    sign = "+";
    color = "#52c41a";
  }

  if (income < 0) {
    color = "#ff4d4f";
  }

  return (
    <span style={{ color }}>
      {sign}
      {moneyRenderer(income)}
    </span>
  );
};

export const genderRenderer = (gender = "DEFAULT") => {
  return {
    NAM: "Nam",
    NU: "Nữ",
    KHAC: "Khác",
    DEFAULT: "Không rõ",
  }[gender];
};

export const employeeTypeRenderer = (type = "DEFAULT") => {
  return {
    MANAGER: "Quản lý",
    DISPATCHER: "NV điều phối",
    REPAIRER: "NV sửa chữa",
    DEFAULT: "Không rõ",
  }[type];
};

export const employeeStatusRenderer = (status = true) => {
  return {
    false: "Đã nghỉ việc",
    true: "Đang làm việc",
  }[status];
};

export const productStatusRenderer = (status = "OUT_STOCK") => {
  return {
    IN_STOCK: "Còn hàng",
    OUT_STOCK: "Hết hàng",
    LOW_STOCK: "Sắp hết",
  }[status];
};

export const historyActionRenderer = (action) => {
  return {
    IMPORT: "Nhập kho",
    EXPORT: "Xuất kho",
  }[action];
};

export const nullRenderer = (canBeNullVal) => {
  return canBeNullVal == null ? "--" : canBeNullVal;
};

export const accountsIsActiveRenderer = (isActive = true) => {
  return {
    false: "Dừng hoạt động",
    true: "Đang hoạt động",
  }[isActive];
};

export const accountRoleRenderer = (role = "DEFAULT") => {
  return {
    DEFAULT: "Không rõ",
    MANAGER: "Quản lý",
    DISPATCHER: "Điều phối viên",
  }[role];
};

export const defaultRenderer = (value) => {
  if (!value && value !== 0) return "---";
  return value;
};

export const differentRenderer = (value) => {
  if (!value) return "0";
  if (value > 0) return "+" + value;
  return value;
};
