import { Avatar, Button, Dropdown, Space } from "antd";
import { Header } from "antd/es/layout/layout";
import { styled } from "styled-components";
import avatar from "../assets/images/avatar.jpg";
import { useContext } from "react";
import AuthContext from "../context/AuthContext";
import { accountRoleRenderer } from "../utils/renderer";
import { useNavigate } from "react-router-dom";

const CustomHeader = styled(Header)`
  &&& {
    top: 0;
    z-index: 10;
    position: sticky;
    padding: 0;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: end;
    box-shadow: 0 0 6px lightgray;
  }
`;

const LogoutButton = styled(Button)`
  text-align: center;
  display: inline-block;
  width: 100%;
`;

const AvatarDropdown = styled(Dropdown)`
  padding: 0 25px;

  &:hover {
    color: #1677ff;
  }
`;

const AppHeader = () => {
  const navigate = useNavigate();
  const { user, logout } = useContext(AuthContext);

  const itemsManager = [
    {
      label: (
        <Button ghost type="primary" onClick={() => navigate(`/accounts/edit/${user?.id}`)} className="w-100">
          Đổi mật khẩu
        </Button>
      ),
      key: "0",
    },
    {
      label: (
        <LogoutButton type="primary" danger onClick={logout}>
          Đăng xuất
        </LogoutButton>
      ),
      key: "1",
    },
  ];

  const itemsDispatcher = [
    {
      label: (
        <LogoutButton type="primary" danger onClick={logout}>
          Đăng xuất
        </LogoutButton>
      ),
      key: "0",
    },
  ];

  return (
    <CustomHeader>
      <AvatarDropdown menu={{ items: user?.role === "MANAGER" ? itemsManager : itemsDispatcher }} trigger={["click"]}>
        <Space>
          <Avatar src={avatar} alt="avatar" size={45} style={{ border: "4px solid #f5f5f5" }} />
          <span>
            {accountRoleRenderer(user.role)} - {user.username}
          </span>
        </Space>
      </AvatarDropdown>
    </CustomHeader>
  );
};

export default AppHeader;
