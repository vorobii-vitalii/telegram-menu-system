#!/bin/bash
OUT_DIR=/home/vitalii/projects/telegram-menu-system/admin-panel-ui/src
cd /home/vitalii/projects/telegram-menu-system/menu-items-service/src/main/proto
echo 'Generating service stubs...'
protoc menu-items-service.proto   --js_out=import_style=commonjs,binary:$OUT_DIR   --grpc-web_out=import_style=typescript,mode=grpcweb:$OUT_DIR