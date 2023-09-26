import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import accountAPI from "../../api/requests/account";
import FormAccount from "../../components/form/FormAccount";

const { Title } = Typography;

const EditEmployee = () => {
  const { accountId } = useParams();
  const [account, setAccount] = useState(null);
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    apiCaller(accountAPI.getById(accountId), (data) => {
      const accountData = data.data;
      setAccount(accountData);
    });
  }, [accountId]);

  const handleEditAccount = (values) => {
    apiCaller(accountAPI.updateById(accountId, values), () => {
      AntdSwal.fire("Thành công", "Cập nhật tài khoản thành công", "success");
      navigate("/accounts");
    });
  };

  return (
    <Container size="lg">
      <Link to="/accounts">
        <LeftOutlined /> Quay lại danh sách nhân viên
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật tài khoản {account?.username}
      </Title>
      <Card title="Thông tin cơ bản" bordered={false} className="mt-8">
        <FormAccount form={form} onFinish={handleEditAccount} initialValues={{ ...account, password: null }} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/accounts")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Lưu
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default EditEmployee;
