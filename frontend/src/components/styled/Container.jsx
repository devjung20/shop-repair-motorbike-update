import { styled } from "styled-components";

const Container = styled.div`
  width: 100%;
  max-width: ${({ size = "full" }) =>
    ({
      full: "100%",
      sm: "576px",
      md: "768px",
      lg: "992px",
      xl: "1200px",
      xxl: "1400px",
    }[size])};
  margin: auto;
`;

export default Container;
