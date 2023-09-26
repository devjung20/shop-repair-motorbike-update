import { Layout } from "antd";
import Title from "antd/es/typography/Title";
import { Link } from "react-router-dom";
import { styled } from "styled-components";

const NotFoundLayout = styled(Layout)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
`;

const NotFound = () => {
  return (
    <NotFoundLayout>
      <img
        src="https://www.pngitem.com/pimgs/m/561-5616833_image-not-found-png-not-found-404-png.png"
        alt="not-found"
        style={{ maxWidth: "min(500px, calc(100vw - 120px))" }}
      />
      <Title level={4}>Trang bạn đang muốn truy cập không tồn tại.</Title>
      <Title level={4} style={{ marginTop: 0 }}>
        Nhấn vào đây để trở lại <Link to="/">trang chủ</Link>.
      </Title>
    </NotFoundLayout>
  );
};

export default NotFound;
