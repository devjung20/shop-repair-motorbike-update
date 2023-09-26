import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import { toYYYY_MM_DD } from "../../utils/converter";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import dayjs from "dayjs";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FromCustomer from "../../components/form/FormCustomer";
import customerAPI from "../../api/requests/customer";

const { Title } = Typography;

const EditCustomer = () => {
  const { customerId } = useParams();
  const [customer, setCustomer] = useState(null);
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    apiCaller(customerAPI.getById(customerId), (data) => {
      const customerData = data.data;
      customerData.birthday = dayjs(customerData.birthday);
      setCustomer(customerData);
    });
  }, [customerId]);

  const handleEditCustomer = (values) => {
    values.birthday = toYYYY_MM_DD(values.birthday);
    apiCaller(customerAPI.updateById(customerId, values), () => {
      AntdSwal.fire("Thành công", "Cập nhật khách hàng thành công", "success");
      navigate("/customers");
    });
  };

  return (
    <Container size="lg">
      <Link to="/customers">
        <LeftOutlined /> Quay lại danh sách khách hàng
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật khách hàng {customer?.code}
      </Title>
      <Card title="Thông tin cơ bản" bordered={false} className="mt-8">
        <FromCustomer form={form} onFinish={handleEditCustomer} initialValues={customer} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/customers")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Lưu
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default EditCustomer;
