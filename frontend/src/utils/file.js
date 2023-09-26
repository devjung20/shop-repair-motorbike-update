import AntdSwal from "../custom/AntdSwal";

export const getBase64 = (file) => {
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });
};

export const beforeUpload = (file) => {
  const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
  if (!isJpgOrPng) {
    AntdSwal.fire("Có lỗi xảy ra", "Ảnh phải có định dạng PNG hoặc JPEG!", "error");
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    AntdSwal.fire("Có lỗi xảy ra", "Kích thước hình ảnh không được quá 2MB!", "error");
  }
  return isJpgOrPng && isLt2M;
};
