import Swal from "sweetalert2";

const AntdSwal = Swal.mixin({
  width: "450px",
  customClass: {
    confirmButton: "ant-btn css-190m0jy ant-btn-primary ant-btn-lg",
    cancelButton: "ant-btn css-190m0jy ant-btn-primary ant-btn-lg ant-btn-dangerous mr-3",
  },
  buttonsStyling: false,
});

export default AntdSwal;
