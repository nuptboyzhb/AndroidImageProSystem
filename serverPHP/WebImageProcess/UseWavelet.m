function UseWavelet(input_img_path, output_img_path)
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
InputImg =imread(input_img_path);
oldbuf=rgb2gray(InputImg);
oldbuf=double(oldbuf);
[M N]=size(oldbuf);
x = wavelet('2D CDF 9/7',3, oldbuf,'asym',1);%采用9/7双正交小波基
x(1:1:M/power(2,3),1:1:N/power(2,3))=0.15*x(1:1:M/power(2,3),1:1:N/power(2,3));
%%
x(M/power(2,3),1:1:2*N/power(2,3))=255;
x(1:1:2*M/power(2,3),N/power(2,3))=255;
%% 
x(2*M/power(2,3),1:1:4*N/power(2,3))=255;
x(1:1:4*M/power(2,3),2*N/power(2,3))=255;
%%
x(4*M/power(2,3),1:1:8*N/power(2,3))=255;
x(1:1:8*M/power(2,3),4*N/power(2,3))=255;
imwrite(uint8(x), output_img_path,'Quality',100);
toc
end
