function ImageDCT(input_img_path, output_img_path)
%-----------------------------------------------------------------------------
% @Author: ZhengHaibo zhb931706659@126.com
% Android Tutorial : Server-Client Communication
%------------------------------------------------------------------------------
% INPUT:
% input_img_path 	- input image path
% output_img_path 	- output image path
%------------------------------------------------------------------------------
tic;
if nargin < 2
    input_img_path =('./upload/test.jpg');
    output_img_path =('./output/test.jpg');
end
if(isempty(input_img_path))
    input_img_path =('./upload/test.jpg');
end
if(isempty(output_img_path))
    output_img_path =('./output/test.jpg');
end
% --------------------------------------------------------------------
%                                                        Load an image
% --------------------------------------------------------------------
InputImg = imread(input_img_path);
GrayInputImg=rgb2gray(InputImg);
DCT_Result=dct2(GrayInputImg)*4;
imwrite(uint8(DCT_Result), output_img_path,'Quality',100);
toc
end
