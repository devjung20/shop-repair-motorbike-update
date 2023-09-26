import { Link, useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import FormEmployee from "../../components/form/FormEmployee";
import { toYYYY_MM_DD } from "../../utils/converter";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import employeeAPI from "../../api/requests/employee";
import FormButtonGroup from "../../components/styled/FormButtonGroup";

const { Title } = Typography;

const AddEmployee = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const handleAddEmployee = (values) => {
    values.birthday = toYYYY_MM_DD(values.birthday);
    apiCaller(employeeAPI.create(values), () => {
      AntdSwal.fire("Thành công", "Thêm nhân viên thành công", "success");
      navigate("/employees");
    });
  };

  return (
    <Container size="lg">
      <Link to="/employees">
        <LeftOutlined /> Quay lại danh sách nhân viên
      </Link>
      <Title level={2} className="mt-4">
        Thêm mới nhân viên
      </Title>
      <Card title="Thông tin nhân viên" bordered={false} className="mt-8">
        <FormEmployee form={form} onFinish={handleAddEmployee} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/employees")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Lưu
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default AddEmployee;
