import { styled } from "styled-components";

const BlankContent = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  & svg {
    font-size: ${({ iconSize }) => iconSize || 150}px;
    color: ${({ iconColor }) => iconColor || "#d3d3d3"};
  }
`;

export default BlankContent;
