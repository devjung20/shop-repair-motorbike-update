import { Col, DatePicker, Form, Input, InputNumber, Row, Select } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { phoneRegex } from "../../constants/regex";
import { useEffect } from "react";

const FormEmployee = ({
  form,
  onFinish,
  initialValues = {
    name: null,
    birthday: null,
    gender: null,
    phone: null,
    email: null,
    address: null,
    salary: null,
    type: null,
    status: null,
  },
}) => {
  const schema = yup.object().shape({
    name: yup.string().trim().required("Vui lòng nhập tên nhân viên"),
    birthday: yup.date().required("Vui lòng nhập ngày sinh"),
    gender: yup.string().trim().required("Vui lòng chọn giới tính"),
    phone: yup
      .string()
      .trim()
      .matches(phoneRegex, "Số điện thoại không hợp lệ")
      .required("Vui lòng nhập số điện thoại"),
    email: yup.string().trim().email("Email không hợp lệ").required("Vui lòng nhập email"),
    address: yup.string().trim().required("Vui lòng nhập địa chỉ"),
    salary: yup.number().positive("Lương nhân viên phải là một số dương").required("Vui lòng nhập lương nhân viên"),
    type: yup.string().trim().required("Vui lòng chọn vai trò"),
    status: yup.boolean().required("Vui lòng chọn trạng thái"),
  });

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
      <Row gutter={12}>
        <Col xs={24} lg={8}>
          <Form.Item label="Họ tên" name="name" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập họ tên" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Ngày sinh" name="birthday" rules={[yupSync(schema)]} required>
            <DatePicker placeholder="Chọn ngày sinh" format="DD/MM/YYYY" className="w-100" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Giới tính" name="gender" rules={[yupSync(schema)]} required>
            <Select
              placeholder="Chọn giới tính"
              options={[
                { value: "NAM", label: "Nam" },
                { value: "NU", label: "Nữ" },
                { value: "KHAC", label: "Khác" },
              ]}
            />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Số điện thoại" name="phone" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập số điện thoại" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Email" name="email" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập email" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Địa chỉ" name="address" rules={[yupSync(schema)]} required>
            <Input placeholder="Nhập địa chỉ" />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Lương cơ bản" name="salary" rules={[yupSync(schema)]} required>
            <InputNumber
              formatter={(value) => value.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
              parser={(value) => value.replace(/₫\s?|(,*)/g, "")}
              placeholder="Nhập lương cơ bản"
              step={1000000}
              addonAfter="₫"
              className="w-100"
            />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Vai trò" name="type" rules={[yupSync(schema)]} required>
            <Select
              placeholder="Chọn vai trò"
              options={[
                { value: "MANAGER", label: "Quản lý" },
                { value: "DISPATCHER", label: "NV điều phối" },
                { value: "REPAIRER", label: "NV sửa chữa" },
              ]}
            />
          </Form.Item>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Form.Item label="Trạng thái" name="status" rules={[yupSync(schema)]} required>
            <Select
              placeholder="Chọn trạng thái"
              options={[
                { value: true, label: "Đang làm việc" },
                { value: false, label: "Đã nghỉ việc" },
              ]}
            />
          </Form.Item>
        </Col>
      </Row>
    </Form>
  );
};

export default FormEmployee;
