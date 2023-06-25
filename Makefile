run-dist:
	app/build/install/app/bin/app $(FILE1) $(FILE2)

.PHONY: build
build:
	make -C app build

report:
	make -C app report