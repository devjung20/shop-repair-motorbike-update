import { Col, Form, Input, InputNumber, Row } from "antd";
import * as yup from "yup";
import { useEffect } from "react";
import { yupSync } from "../../utils/yup";

const FormProduct = ({
  form,
  onFinish,
  initialValues = {
    name: null,
    brand: null,
    priceIn: null,
    priceOut: null,
    unit: null,
    storageQuantity: null,
    quantityWarning: null,
    description: null,
  },
}) => {
  const schema = yup.object().shape({
    name: yup.string().trim().required("Vui lòng nhập tên linh kiện"),
    brand: yup.string().trim(),
    priceIn: yup.number().min(0, "Giá nhập tối thiểu là 0").required("Giá nhập là bắt buộc"),
    priceOut: yup.number().min(0, "Giá bán tối thiểu là 0").required("Giá bán là bắt buộc"),
    unit: yup.string().trim().required("Vui lòng nhập đơn vị"),
    storageQuantity: yup
      .number()
      .min(0, "Số lượng tối thiểu là 0")
      .integer("Số lượng phải là số nguyên")
      .required("Vui lòng nhập số lượng"),
    quantityWarning: yup
      .number()
      .min(0, "Số lượng tối thiểu không được nhỏ hơn 0")
      .integer("Số lượng tối thiểu phải là số nguyên")
      .required("Vui lòng nhập số lượng tối thiểu"),
    description: yup.string(),
  });

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <Row gutter={12}>
        <Col xs={24} lg={12}>
          <Form.Item label="Tên linh kiện" name="name" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập tên linh kiện" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Thương hiệu" name="brand" rules={[yupSync(schema)]}>
            <Input placeholder="Nhập tên thương hiệu" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Giá nhập" name="priceIn" rules={[yupSync(schema)]} required>
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Giá nhập kho của linh kiện"
              step={1000}
              addonAfter="₫"
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col xs={24} lg={12}>
          <Form.Item label="Giá xuất" name="priceOut" rules={[yupSync(schema)]} required>
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Giá xuất kho của linh kiện"
              step={1000}
              addonAfter="₫"
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col xs={24} lg={8}>
          <Form.Item label="Đơn vị" name="unit" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập đơn vị" />
          </Form.Item>
        </Col>
        <Col xs={24} lg={8}>
          <Form.Item
            label={!initialValues?.storageQuantity ? "Số lượng khởi tạo" : "Số lượng"}
            name="storageQuantity"
            rules={[yupSync(schema)]}
            required
          >
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Nhập số lượng khởi tạo"
              step={1}
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col xs={24} lg={8}>
          <Form.Item label="Số lượng tối thiểu" name="quantityWarning" rules={[yupSync(schema)]} required>
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Nhập số lượng cảnh báo"
              step={1}
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col span={24}>
          <Form.Item label="Mô tả" name="description" rules={[yupSync(schema)]}>
            <Input.TextArea rows={3} placeholder="Nhập mô tả" />
          </Form.Item>
        </Col>
      </Row>
    </Form>
  );
};

export default FormProduct;
