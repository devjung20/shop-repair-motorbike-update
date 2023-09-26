import { Col, Row } from "antd";
import Title from "antd/es/typography/Title";
import { styled } from "styled-components";
import { useEffect, useState } from "react";

import bgLoginCard from "../assets/images/bg_login-card.jpg";
import bgForgotPasswordCard from "../assets/images/bg_forgot-password-card.jpg";
import FormLogin from "../components/form/FormLogin";
import FormForgotPassword from "../components/form/FormForgotPassword";
import FormVerifyCode from "../components/form/FormVerifyCode";
import { useNavigate, useParams } from "react-router-dom";

const StyledCard = styled(Row)`
  background-color: #fff;
  border-radius: 1rem;
  width: min(840px, 90vw);
  height: 540px;
  opacity: ${({ hide }) => (hide ? 0 : 1)};
  transition: opacity 500ms;
`;

const FormWrapper = styled.div`
  border-radius: 1rem;
  background: linear-gradient(to bottom right, #98bbff, #dbeeff);
  box-shadow: 0 0 5px lightgray;
  padding: 0 calc(50% - min(160px, 45%));
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 5;

  @media (min-width: 576px) {
    transform: ${({ action }) => (action === "login" ? "translateX(0)" : "translateX(calc(100% + 2rem))")};
    transition: transform 0.5s;
  }
`;

const CustomCol = styled(Col)`
  background-image: url(${bgForgotPasswordCard});
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
`;

const SideBackground = styled(Col)`
  background-image: url(${bgLoginCard});
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 1rem;
`;

const Auth = () => {
  const navigate = useNavigate();
  const { action = "login" } = useParams();

  const [hide, setHide] = useState(true);
  const [verifyForm, setVerifyForm] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setHide(false), 500);

    return () => {
      clearTimeout(timer);
    };
  }, []);

  const forgotPasswordNavigator = () => navigate("/auth/forgot-password");
  const loginNavigator = () => navigate("/auth/login");
  const showVerifyForm = () => setVerifyForm(true);
  const hideVerifyForm = () => setVerifyForm(false);

  return (
    <StyledCard hide={hide}>
      <CustomCol xs={24} sm={12} className="p-4">
        <FormWrapper action={action}>
          {action === "login" && (
            <>
              <Title level={2}>Đăng nhập</Title>
              <FormLogin navigator={forgotPasswordNavigator} />
            </>
          )}
          {action === "forgot-password" && !verifyForm && (
            <>
              <Title level={2}>Quên mật khẩu</Title>
              <FormForgotPassword navigator={loginNavigator} showVerifyForm={showVerifyForm} />
            </>
          )}
          {action === "forgot-password" && verifyForm && (
            <>
              <Title level={2}>Nhập mã xác thực</Title>
              <FormVerifyCode navigator={loginNavigator} hideVerifyForm={hideVerifyForm} />
            </>
          )}
        </FormWrapper>
      </CustomCol>
      <SideBackground xs={0} sm={12} />
    </StyledCard>
  );
};

export default Auth;
