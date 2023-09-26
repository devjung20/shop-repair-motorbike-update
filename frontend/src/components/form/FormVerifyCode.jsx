import * as yup from "yup";
import { Button, Form, Input } from "antd";
import { yupSync } from "../../utils/yup";
import AntdSwal from "../../custom/AntdSwal";
import AuthContext from "../../context/AuthContext";
import { useContext } from "react";

const FormVerifyCode = ({ navigator, hideVerifyForm }) => {
  const [form] = Form.useForm();
  const { sendCodeToSever } = useContext(AuthContext);

  const schema = yup.object().shape({
    code: yup.string().required("Vui lòng nhập mã xác thực"),
  });

  const handleSubmit = (values) => {
    sendCodeToSever(values);
    AntdSwal.fire(
      "Thành công",
      "Mật khẩu đã được đặt lại. Vui lòng đăng nhập với mật khẩu mới được gửi tới email của bạn",
      "success"
    );
    hideVerifyForm();
    navigator();
  };

  return (
    <Form form={form} layout="vertical" onFinish={handleSubmit}>
      <div className="mb-4">
        <i>
          Chúng tôi đã gửi mã xác thực tới email bạn cung cấp. Vui lòng kiểm tra hòm thư của bạn. Mã xác thực có hiệu
          lực 5 phút.
        </i>
      </div>
      <Form.Item label="Mã xác thực" name="code" rules={[yupSync(schema)]}>
        <Input placeholder="Nhập mã xác thực" size="large" />
      </Form.Item>
      <Button type="primary" htmlType="submit" size="large" className="w-100 mt-2">
        Lấy lại mật khẩu
      </Button>
      <Button type="link" className="w-100 mt-4" onClick={navigator}>
        Thử đăng nhập lại
      </Button>
    </Form>
  );
};

export default FormVerifyCode;
