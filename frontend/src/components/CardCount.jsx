import { Card, Typography } from "antd";
import { styled } from "styled-components";
const { Title } = Typography;

const StyledCard = styled(Card)`
  color: ${({ color }) => (color ? "#fff" : "#000")};
  background-color: ${({ color }) => (color ? color : "#fff")};
  border-radius: 5rem;
  width: 100%;
`;

const StyledTitle = styled(Title)`
  color: ${({ color }) => (color ? "#fff" : "#000")} !important;
  margin-bottom: 0 !important;
`;

const CardInner = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

const IconWrapper = styled.div`
  aspect-ratio: 1/1;
  background-color: rgba(255, 255, 255, 0.35);
  border-radius: 100%;
  width: fit-content;
  padding: 1rem;

  & svg {
    font-size: 25px;
  }
`;

const CardCount = ({ title, count, icon, color }) => {
  return (
    <StyledCard color={color}>
      <CardInner>
        <IconWrapper>{icon}</IconWrapper>
        <div>
          <div>{title}</div>
          <StyledTitle level={3} color={color}>
            {count}
          </StyledTitle>
        </div>
      </CardInner>
    </StyledCard>
  );
};

export default CardCount;
