import { Col, Form, Input, Row, Select } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { useEffect, useRef } from "react";

const FormAccount = ({
  form,
  onFinish,
  initialValues = {
    username: null,
    email: null,
    role: null,
    password: null,
  },
}) => {
  const passwordRef = useRef();

  const accountShape = {
    username: yup.string().trim().required("Vui lòng nhập tên tài khoản"),
    email: yup.string().trim().email("Email không hợp lệ").required("Vui lòng nhập email"),
    role: yup.string().required("Vui lòng chọn vai trò"),
    password: yup.string().min(4, "Mật khẩu phải dài hơn 4 ký tự").max(10, "Mật khẩu phải chứa không quá 10 ký tự"),
  };

  if (!initialValues?.username) {
    accountShape.password = accountShape.password.required("Vui lòng nhập mật khẩu");
  } else {
    accountShape.password = accountShape.password.nullable();
  }

  const schema = yup.object().shape(accountShape);
  const accountRoleEditable = !initialValues?.username;

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <input type="password" style={{ position: "absolute", top: -1000 }} />
      <Row gutter={12}>
        <Col xs={24} md={12}>
          <Form.Item label="Tên tài khoản" name="username" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập tên tài khoản" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item label="Email" name="email" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập email" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item label="Vai trò" name="role" rules={[yupSync(schema)]} required>
            <Select placeholder="Chọn vai trò" disabled={!accountRoleEditable}>
              <Select.Option value="MANAGER">Quản lý</Select.Option>
              <Select.Option value="DISPATCHER">Điều phối viên</Select.Option>
            </Select>
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item
            label="Mật khẩu"
            name="password"
            rules={[yupSync(schema)]}
            required={Boolean(!initialValues?.username)}
          >
            <Input.Password
              placeholder={initialValues?.username ? "Bỏ trống nếu không muốn đổi mật khẩu" : "Nhập mật khẩu"}
              ref={passwordRef}
              autoComplete="off"
            />
          </Form.Item>
        </Col>
      </Row>
    </Form>
  );
};

export default FormAccount;
