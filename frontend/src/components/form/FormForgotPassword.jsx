import * as yup from "yup";
import { Button, Form, Input } from "antd";
import { yupSync } from "../../utils/yup";
import authAPI from "../../api/requests/auth";
import { apiCaller } from "../../utils/api";

const FormForgotPassword = ({ navigator, showVerifyForm }) => {
  const [form] = Form.useForm();
  const schema = yup.object().shape({
    email: yup.string().email("Email không hợp lệ").required("Vui lòng nhập email"),
  });

  const sendEmailToSever = async (data) => {
    await apiCaller(authAPI.sendEmail(data), () => {
      showVerifyForm();
    });
  };

  const handleSubmit = (values) => {
    // AntdSwal.fire("Thành công", "Email đã nhập là " + values.email, "success");
    sendEmailToSever(values);
    // navigate()
  };

  return (
    <Form form={form} layout="vertical" onFinish={handleSubmit}>
      <div className="mb-4">
        <i>
          Chúng tôi sẽ gửi một mã xác thực tới địa chỉ email bạn cung cấp. Vui lòng đảm bảo email của tài khoản là chính
          xác.
        </i>
      </div>
      <Form.Item label="Email" name="email" rules={[yupSync(schema)]}>
        <Input placeholder="Nhập địa chỉ email" size="large" />
      </Form.Item>
      <Button type="primary" htmlType="submit" size="large" className="w-100 mt-2">
        Gửi mã xác thực
      </Button>
      <Button type="link" className="w-100 mt-4" onClick={navigator}>
        Thử đăng nhập lại
      </Button>
    </Form>
  );
};

export default FormForgotPassword;
