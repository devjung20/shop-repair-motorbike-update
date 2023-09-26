import { Col, Form, Input, InputNumber, Row } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { useEffect } from "react";

const FormService = ({
  form,
  onFinish,
  initialValues = {
    name: null,
    price: null,
    description: null,
  },
}) => {
  const schema = yup.object().shape({
    name: yup.string().trim().required("Vui lòng nhập tên dịch vụ"),
    price: yup.number().min(0, "Giá dịch vụ tối thiểu là 0").required("Vui lòng nhập giá dịch vụ"),
    description: yup.string().trim().nullable(),
  });

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <Row gutter={12}>
        <Col xs={24} lg={12}>
          <Form.Item label="Tên dịch vụ" name="name" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập tên dịch vụ" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Giá dịch vụ" name="price" rules={[yupSync(schema)]} required>
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Nhập giá của dịch vụ"
              step={1000}
              addonAfter="₫"
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col span={24}>
          <Form.Item label="Mô tả" name="description" rules={[yupSync(schema)]}>
            <Input.TextArea rows={3} placeholder="Nhập mô tả dịch vụ" />
          </Form.Item>
        </Col>
      </Row>
    </Form>
  );
};

export default FormService;
