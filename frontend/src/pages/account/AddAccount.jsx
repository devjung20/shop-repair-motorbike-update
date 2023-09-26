import { Link, useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormAccount from "../../components/form/FormAccount";
import authAPI from "../../api/requests/auth";

const { Title } = Typography;

const AddEmployee = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const handleAddAccount = (values) => {
    apiCaller(authAPI.register(values), () => {
      AntdSwal.fire("Thành công", "Thêm tài khoản thành công", "success");
      navigate("/accounts");
    });
  };

  return (
    <Container size="lg">
      <Link to="/accounts">
        <LeftOutlined /> Quay lại danh sách tài khoản
      </Link>
      <Title level={2} className="mt-4">
        Thêm mới tài khoản
      </Title>
      <Card title="Thêm tài khoản" bordered={false} className="mt-8">
        <FormAccount form={form} onFinish={handleAddAccount} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/accounts")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Thêm tài khoản
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default AddEmployee;
