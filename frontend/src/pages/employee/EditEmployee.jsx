import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import FormEmployee from "../../components/form/FormEmployee";
import { toYYYY_MM_DD } from "../../utils/converter";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import employeeAPI from "../../api/requests/employee";
import dayjs from "dayjs";
import FormButtonGroup from "../../components/styled/FormButtonGroup";

const { Title } = Typography;

const EditEmployee = () => {
  const { employeeId } = useParams();
  const [employee, setEmployee] = useState(null);
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    apiCaller(employeeAPI.getById(employeeId), (data) => {
      const employeeData = data.data;
      employeeData.birthday = dayjs(employeeData.birthday);
      setEmployee(employeeData);
    });
  }, [employeeId]);

  const handleEditEmployee = (values) => {
    values.birthday = toYYYY_MM_DD(values.birthday);
    apiCaller(employeeAPI.updateById(employeeId, values), () => {
      AntdSwal.fire("Thành công", "Cập nhật nhân viên thành công", "success");
      navigate("/employees");
    });
  };

  return (
    <Container size="lg">
      <Link to="/employees">
        <LeftOutlined /> Quay lại danh sách nhân viên
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật nhân viên {employee?.code}
      </Title>
      <Card title="Thông tin cơ bản" bordered={false} className="mt-8">
        <FormEmployee form={form} onFinish={handleEditEmployee} initialValues={employee} />
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

export default EditEmployee;
