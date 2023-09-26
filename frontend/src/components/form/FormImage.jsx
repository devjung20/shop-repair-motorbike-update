import { Upload } from "antd";
import { useState } from "react";
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons";
import { beforeUpload } from "../../utils/file";
import axios from "axios";
import { getJwtToken } from "../../utils/token";
import { styled } from "styled-components";

const UploadButton = ({ loading }) => {
  return (
    <div>
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div className="mt-2">Thêm hình ảnh</div>
    </div>
  );
};

const ImagePreview = styled.div`
  background-image: ${({ src }) => "url('" + src + "')"};
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  width: 100%;
  aspect-ratio: 1;
`;

const FormImage = ({ imageUrl, setImageUrl }) => {
  const [loading, setLoading] = useState(false);

  const handleUpload = ({ onSuccess, onError, file, onProgress }) => {
    setLoading(true);
    const fmData = new FormData();
    const config = {
      headers: { "content-type": "multipart/form-data" },
      onUploadProgress: (event) => {
        onProgress({ percent: (event.loaded / event.total) * 100 }, file);
      },
    };
    const token = getJwtToken();
    if (token) config.headers.Authorization = "Bearer " + token;
    fmData.append("file", file);
    axios
      .post("http://localhost:8080/api/products/upload", fmData, config)
      .then((res) => {
        setImageUrl(res?.data?.data?.url);
        setLoading(false);
        onSuccess(file);
      })
      .catch((err) => {
        const error = new Error(err.message);
        setLoading(false);
        onError({ event: error });
      });
  };

  return (
    <Upload
      action="https://localhost:8080/api/products/upload"
      accept="image/png, image/jpeg"
      listType="picture-card"
      showUploadList={false}
      beforeUpload={beforeUpload}
      onChange={() => {}}
      customRequest={handleUpload}
    >
      {imageUrl && <ImagePreview src={imageUrl} />}
      {!imageUrl && <UploadButton loading={loading} />}
    </Upload>
  );
};

export default FormImage;
