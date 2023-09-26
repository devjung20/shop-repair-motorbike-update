import * as yup from "yup";
import { useEffect } from "react";
import { yupSync } from "../../utils/yup";
import { Col, Form, Input, Row } from "antd";
import { emptyPhoneRegex } from "../../constants/regex";

const FormCustomer = ({
  form,
  onFinish,
  initialValues = {
    name: null,
    phone: null,
    email: null,
    address: null,
  },
}) => {
  const schema = yup.object().shape({
    name: yup.string().trim().required("Vui lòng nhập tên khách hàng"),
    phone: yup.string().nullable().matches(emptyPhoneRegex, "Số điện thoại không hợp lệ"),
    email: yup.string().nullable().email("Email không hợp lệ"),
    address: yup.string().trim().nullable(),
  });

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <Row gutter={12}>
        <Col xs={24} lg={12}>
          <Form.Item label="Tên khách hàng" name="name" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập tên khách hàng" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Số điện thoại" name="phone" rules={[yupSync(schema)]}>
            <Input placeholder="Nhập số điện thoại" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Email" name="email" rules={[yupSync(schema)]}>
            <Input placeholder="Nhập email" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Địa chỉ" name="address" rules={[yupSync(schema)]}>
            <Input placeholder="Nhập địa chỉ" />
          </Form.Item>
        </Col>
      </Row>
    </Form>
  );
};

export default FormCustomer;
