import { useContext } from "react";
import AuthContext from "../../context/AuthContext";
import { Button, Form, Input } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { useNavigate } from "react-router-dom";

const FormLogin = ({ navigator }) => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const [form] = Form.useForm();

  const schema = yup.object().shape({
    email: yup.string().trim().email("Email không hợp lệ").required("Vui lòng nhập email"),
    password: yup.string().required("Vui lòng nhập mật khẩu"),
  });

  const handleSubmit = (values) => {
    login(values);
    navigate("/");
  };

  return (
    <Form form={form} layout="vertical" onFinish={handleSubmit} className="w-100">
      <Form.Item label="Email" name="email" rules={[yupSync(schema)]}>
        <Input placeholder="Nhập địa chỉ email" size="large" />
      </Form.Item>
      <Form.Item label="Mật khẩu" name="password" rules={[yupSync(schema)]}>
        <Input.Password placeholder="Nhập mật khẩu" size="large" />
      </Form.Item>
      <Button type="primary" htmlType="submit" size="large" className="w-100 mt-2">
        Đăng nhập
      </Button>
      <Button type="link" className="w-100 mt-4" onClick={navigator}>
        Quên mật khẩu?
      </Button>
    </Form>
  );
};

export default FormLogin;
