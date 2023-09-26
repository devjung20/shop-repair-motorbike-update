import { Col, Form, Input, Modal, Row, Select } from "antd";
import * as yup from "yup";
import { yupSync } from "../../utils/yup";
import { useEffect, useState } from "react";
import { apiCaller } from "../../utils/api";
import customerAPI from "../../api/requests/customer";
import employeeAPI from "../../api/requests/employee";
import { PlusOutlined } from "@ant-design/icons";
import FormCustomer from "./FormCustomer";
import AntdSwal from "../../custom/AntdSwal";

const FormOrderInfo = ({
  form,
  onFinish,
  initialValues = {
    customerId: null,
    dispatcherId: null,
    repairerId: null,
    motorbikeName: null,
    motorbikeCode: null,
    note: null,
  },
}) => {
  const schema = yup.object().shape({
    customerId: yup.number().required("Vui lòng chọn khách hàng"),
    dispatcherId: yup.number().required("Vui lòng chọn nhân viên điều phối"),
    repairerId: yup.number().required("Vui lòng chọn nhân viên sửa chữa"),
    motorbikeName: yup.string().trim().required("Vui lòng nhập tên xe"),
    motorbikeCode: yup.string().trim().required("Vui lòng biển số xe"),
    note: yup.string().nullable(),
  });

  const [customerOptions, setCustomerOptions] = useState([]);
  const [dispatcherOptions, setDispatcherOptions] = useState([]);
  const [repairerOptions, setRepairerOptions] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [session, setSession] = useState(Math.random());
  const [formCustomer] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue(initialValues);
  }, [form, initialValues]);

  useEffect(() => {
    apiCaller(customerAPI.getAll({ pageSize: 1000, pageNumber: 1 }), (data) => {
      const createOption = {
        value: undefined,
        title: null,
        label: (
          <div className="text-semibold" onClick={() => setIsModalOpen(true)}>
            <PlusOutlined /> Thêm khách hàng mới
          </div>
        ),
      };
      const options = data?.data?.content?.map((opt) => ({
        label: opt.phone ? [opt.name, opt.phone].join(" - ") : [opt.name, opt.code].join(" - "),
        title: opt.phone ? [opt.name, opt.phone].join(" - ") : [opt.name, opt.code].join(" - "),
        value: opt.id,
      }));
      setCustomerOptions([createOption, ...options]);
    });
  }, [isModalOpen, session]);

  useEffect(() => {
    apiCaller(
      employeeAPI.getAll({
        pageSize: 1000,
        pageNumber: 1,
        type: "REPAIRER",
        status: true,
      }),
      (data) => {
        const options = data?.data?.content?.map((opt) => ({
          label: [opt.name, opt.code].join(" - "),
          title: [opt.name, opt.code].join(" - "),
          value: opt.id,
        }));
        setRepairerOptions(options);
      }
    );
  }, []);

  useEffect(() => {
    apiCaller(
      employeeAPI.getAll({
        pageSize: 1000,
        pageNumber: 1,
        status: true,
      }),
      (data) => {
        const options = data?.data?.content
          ?.filter((o) => o.type === "DISPATCHER" || o.type === "MANAGER")
          ?.map((opt) => ({
            label: [opt.name, opt.code].join(" - "),
            title: [opt.name, opt.code].join(" - "),
            value: opt.id,
          }));
        setDispatcherOptions(options);
      }
    );
  }, []);

  const handleAddCustomer = (values) => {
    if (!values.email) values.email = null;
    if (!values.phone) values.phone = null;
    if (!values.address) values.address = null;
    apiCaller(customerAPI.create(values), () => {
      AntdSwal.fire("Thành công", "Thêm khách hàng thành công", "success");
      setSession(Math.random());
    });
    setIsModalOpen(false);
  };

  const handleCancelAdd = () => {
    formCustomer.resetFields();
    setIsModalOpen(false);
  };

  return (
    <>
      <Modal
        title="Thêm khách hàng mới"
        centered
        cancelText="Hủy"
        okText="Thêm khách hàng"
        open={isModalOpen}
        onOk={() => formCustomer.submit()}
        onCancel={handleCancelAdd}
      >
        <div className="mt-8" />
        <FormCustomer form={formCustomer} onFinish={handleAddCustomer} />
      </Modal>
      <Form form={form} layout="vertical" onFinish={onFinish} initialValues={initialValues}>
        <Row gutter={12}>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Khách hàng" name="customerId" rules={[yupSync(schema)]} required>
              <Select
                placeholder="Chọn khách hàng"
                options={customerOptions}
                showSearch
                filterOption={(input, option) => {
                  if (!option.value) return true;
                  return (option?.label ?? "").toLowerCase().includes(input.toLowerCase());
                }}
                optionFilterProp="children"
                optionLabelProp="title"
              />
            </Form.Item>
          </Col>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Nhân viên điều phối" name="dispatcherId" rules={[yupSync(schema)]} required>
              <Select
                placeholder="Chọn nhân viên điều phối"
                options={dispatcherOptions}
                showSearch
                filterOption={(input, option) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase())}
                optionFilterProp="children"
                optionLabelProp="title"
              />
            </Form.Item>
          </Col>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Nhân viên sửa chữa" name="repairerId" rules={[yupSync(schema)]} required>
              <Select
                placeholder="Chọn nhân viên sửa chữa"
                options={repairerOptions}
                showSearch
                filterOption={(input, option) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase())}
                optionFilterProp="children"
                optionLabelProp="title"
              />
            </Form.Item>
          </Col>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Tên xe" name="motorbikeName" rules={[yupSync(schema)]} required>
              <Input placeholder="Nhập tên xe" />
            </Form.Item>
          </Col>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Biển số xe" name="motorbikeCode" rules={[yupSync(schema)]} required>
              <Input placeholder="Nhập biển số xe" />
            </Form.Item>
          </Col>
          <Col xs={24} md={12} lg={8}>
            <Form.Item label="Ghi chú" name="note" rules={[yupSync(schema)]}>
              <Input placeholder="Nhập ghi chú" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </>
  );
};

export default FormOrderInfo;
