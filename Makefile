run-dist:
	app/build/install/app/bin/app

.PHONY: build
build:
	make -C app build

report:
	make -C app report