import { Col, Form, Input, Row } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { phoneRegex } from "../../constants/regex";
import { useEffect } from "react";

const FormStore = ({
  form,
  onFinish,
  initialValues = {
    name: null,
    phone: null,
    address: null,
    email: null,
    vat: null,
  },
}) => {
  const schema = yup.object().shape({
    name: yup.string().trim().required("Vui lòng nhập tên cửa hàng"),
    phone: yup
      .string()
      .trim()
      .matches(phoneRegex, "Số điện thoại không hợp lệ")
      .required("Vui lòng nhập số điện thoại"),
    email: yup.string().trim().email("Email không hợp lệ").required("Vui lòng nhập email"),
    address: yup.string().trim().required("Vui lòng nhập địa chỉ"),
    vat: yup.number().positive("VAT không được âm").required("Vui lòng nhập VAT"),
  });

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <Row gutter={12}>
        <Col xs={24} md={12}>
          <Form.Item label="Tên cửa hàng" name="name" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập tên cửa hàng" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item label="Số điện thoại" name="phone" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập số điện thoại" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item label="Email" name="email" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập email" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12}>
          <Form.Item label="Địa chỉ" name="address" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập địa chỉ" />
          </Form.Item>
        </Col>
        {/* <Col xs={24} md={12}>
          <Form.Item label="VAT (%)" name="vat" rules={[yupSync(schema)]} required>
            <InputNumber placeholder="Nhập VAT" step={0.1} addonAfter="%" className="w-100" />
          </Form.Item>
        </Col> */}
      </Row>
    </Form>
  );
};

export default FormStore;
