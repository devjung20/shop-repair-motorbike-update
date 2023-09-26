import { Link, useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import customerAPI from "../../api/requests/customer";
import FormCustomer from "../../components/form/FormCustomer";

const { Title } = Typography;

const AddCustomer = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const handleAddCustomer = (values) => {
    if (!values.email) values.email = null;
    if (!values.phone) values.phone = null;
    if (!values.address) values.address = null;
    apiCaller(customerAPI.create(values), () => {
      AntdSwal.fire("Thành công", "Thêm khách hàng thành công", "success");
      navigate("/customers");
    });
  };

  return (
    <Container size="lg">
      <Link to="/customers">
        <LeftOutlined /> Quay lại danh sách khách hàng
      </Link>
      <Title level={2} className="mt-4">
        Thêm mới khách hàng
      </Title>
      <Card title="Thông tin khách hàng" bordered={false} className="mt-8">
        <FormCustomer form={form} onFinish={handleAddCustomer} />
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

export default AddCustomer;
